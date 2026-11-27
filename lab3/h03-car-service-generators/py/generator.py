"""Car generator for the Car Service lab.

Writes the cars to one file (cars.json or cars.txt) and the expected
statistics to stats.json. It never deletes anything.

    python3 generator.py                         # 30 cars, cars.json
    python3 generator.py --format lines          # 30 cars, cars.txt
    python3 generator.py --seed 42 --chaos 5     # same cars every time, 5 broken
    python3 generator.py --help

The same seed gives the same cars in generator.py and generator.cpp.
"""

import argparse
import time

MASK = (1 << 64) - 1

CAR_TYPES = ["ELECTRIC", "GAS"]
PASSENGER_TYPES = ["PEOPLE", "ROBOTS"]
CONSUMPTION_MIN, CONSUMPTION_MAX = 10, 50

# The ways a car can be broken (--chaos).
CHAOS_KINDS = 5  # unknown type, null passengers, negative consumption,
# missing isDining, consumption that is not a number

MISSING = object()  # the field is not written at all


class Random:
    """SplitMix64. Small, and easy to write the same way in C++."""

    def __init__(self, seed):
        self.state = seed & MASK

    def next(self):
        self.state = (self.state + 0x9E3779B97F4A7C15) & MASK
        z = self.state
        z = ((z ^ (z >> 30)) * 0xBF58476D1CE4E5B9) & MASK
        z = ((z ^ (z >> 27)) * 0x94D049BB133111EB) & MASK
        return z ^ (z >> 31)

    def below(self, n):
        return self.next() % n


def make_car(rng):
    return {
        "type": CAR_TYPES[rng.below(2)],
        "passengers": PASSENGER_TYPES[rng.below(2)],
        "isDining": rng.below(2) == 0,
        "consumption": CONSUMPTION_MIN
        + rng.below(CONSUMPTION_MAX - CONSUMPTION_MIN + 1),
    }


def break_car(car, rng):
    kind = rng.below(CHAOS_KINDS)
    if kind == 0:
        car["type"] = "HYDROGEN"
    elif kind == 1:
        car["passengers"] = None
    elif kind == 2:
        car["consumption"] = -car["consumption"]
    elif kind == 3:
        car["isDining"] = MISSING
    else:
        car["consumption"] = "lots"
    return car


def json_value(value):
    if value is None:
        return "null"
    if isinstance(value, bool):
        return "true" if value else "false"
    if isinstance(value, int):
        return str(value)
    return '"' + value + '"'


def line_value(value):
    # In the lines format, null is the word null, true/false are words.
    return json_value(value).strip('"')


FIELDS = ["id", "type", "passengers", "isDining", "consumption"]


def to_json_line(car):
    parts = [f'"{k}": {json_value(car[k])}' for k in FIELDS if car[k] is not MISSING]
    return "{" + ", ".join(parts) + "}"


def to_text_line(car):
    return " ".join(line_value(car[k]) for k in FIELDS if car[k] is not MISSING)


def stats_json(stats):
    return (
        "{\n"
        f'    "SEED": {stats["SEED"]},\n'
        f'    "TOTAL": {stats["TOTAL"]},\n'
        f'    "REJECTED": {stats["REJECTED"]},\n'
        f'    "ELECTRIC": {stats["ELECTRIC"]},\n'
        f'    "GAS": {stats["GAS"]},\n'
        f'    "PEOPLE": {stats["PEOPLE"]},\n'
        f'    "ROBOTS": {stats["ROBOTS"]},\n'
        f'    "DINING": {stats["DINING"]},\n'
        f'    "NOT_DINING": {stats["NOT_DINING"]},\n'
        f'    "DINING_BY": {{"PEOPLE": {stats["DINING_PEOPLE"]}, "ROBOTS": {stats["DINING_ROBOTS"]}}},\n'
        f'    "CONSUMPTION": {{"ELECTRIC": {stats["CONS_ELECTRIC"]}, "GAS": {stats["CONS_GAS"]}}}\n'
        "}\n"
    )


def main():
    parser = argparse.ArgumentParser(
        description="Generate cars for the Car Service lab."
    )
    parser.add_argument(
        "--count", type=int, default=30, help="number of valid cars (default 30)"
    )
    parser.add_argument(
        "--chaos", type=int, default=0, help="number of broken cars to add (default 0)"
    )
    parser.add_argument(
        "--seed",
        type=int,
        default=None,
        help="same seed, same cars (default: current time)",
    )
    parser.add_argument(
        "--format",
        choices=["json", "lines"],
        default="json",
        help="json: cars.json, lines: cars.txt with one car per line",
    )
    args = parser.parse_args()

    if args.count < 0 or args.chaos < 0:
        parser.error("--count and --chaos cannot be negative")
    seed = args.seed if args.seed is not None else int(time.time())
    if seed < 0:
        parser.error("--seed cannot be negative")

    rng = Random(seed)
    cars = [make_car(rng) for _ in range(args.count)]
    cars += [break_car(make_car(rng), rng) for _ in range(args.chaos)]

    # Fisher-Yates shuffle, so the broken cars are mixed in.
    for i in range(len(cars) - 1, 0, -1):
        j = rng.below(i + 1)
        cars[i], cars[j] = cars[j], cars[i]

    stats = dict.fromkeys(
        [
            "ELECTRIC",
            "GAS",
            "PEOPLE",
            "ROBOTS",
            "DINING",
            "NOT_DINING",
            "DINING_PEOPLE",
            "DINING_ROBOTS",
            "CONS_ELECTRIC",
            "CONS_GAS",
        ],
        0,
    )
    stats.update(SEED=seed, TOTAL=len(cars), REJECTED=args.chaos)

    for number, car in enumerate(cars, start=1):
        car["id"] = number

    for car in filter(is_valid, cars):
        stats[car["type"]] += 1
        stats[car["passengers"]] += 1
        if car["isDining"]:
            stats["DINING"] += 1
            stats["DINING_" + car["passengers"]] += 1
        else:
            stats["NOT_DINING"] += 1
        stats["CONS_" + car["type"]] += car["consumption"]

    if args.format == "json":
        name = "cars.json"
        body = "[\n" + ",\n".join("    " + to_json_line(c) for c in cars) + "\n]\n"
    else:
        name = "cars.txt"
        body = "".join(to_text_line(c) + "\n" for c in cars)

    with open(name, "w", newline="\n") as f:
        f.write(body)
    with open("stats.json", "w", newline="\n") as f:
        f.write(stats_json(stats))

    print(f"Wrote {len(cars)} cars to {name} ({args.chaos} broken), seed {seed}")
    print(stats_json(stats), end="")


def is_valid(car):
    return (
        car["type"] in CAR_TYPES
        and car["passengers"] in PASSENGER_TYPES
        and isinstance(car["isDining"], bool)
        and isinstance(car["consumption"], int)
        and not isinstance(car["consumption"], bool)
        and CONSUMPTION_MIN <= car["consumption"] <= CONSUMPTION_MAX
    )


if __name__ == "__main__":
    main()
