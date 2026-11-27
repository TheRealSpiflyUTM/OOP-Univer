# Car generator for the Car Service lab

> HEADS UP: if you are reading this on your local machine, your IDE might support or might not MARKDOWN PREVIEW.
> For convenience: https://github.com/domeeno/oo-live-coding/tree/main/h03-car-service-generators README previews are
> supported in github. Open the above URL, for easier reading, cheers - oop lectirer.

Pick **one** generator: `py/generator.py` or `cpp/generator.cpp`. Both make the same cars from
the same seed, and neither needs a library.

The generator writes its files into the folder you run it from, and **never deletes anything**:

| File | What it holds |
|---|---|
| `cars.json` or `cars.txt` | the cars your program must serve |
| `stats.json` | what your program should report at the end |

The two configs from the handout are here too: `stations-good.conf` and `stations-broken.conf`.

*Never run scripts strangers hand you on the internet without reading the source first.
That includes this one.*

## Run it

Python (3.8 or newer):

```bash
python3 py/generator.py                  # or: python / py on Windows
```

C++ (any C++17 compiler, see [`cpp/README.md`](cpp/README.md)):

```bash
g++ -std=c++17 cpp/generator.cpp -o generator
./generator # generator.exe on Windows? Maybe...
```

## Options

| Option | Default | Meaning |
|---|---|---|
| `--count N` | 30 | number of valid cars |
| `--chaos N` | 0 | number of **broken** cars, mixed in with the valid ones |
| `--seed N` | current time | the same seed always gives the same cars |
| `--format json\|lines` | `json` | `json` writes `cars.json`, `lines` writes `cars.txt` |

Example: `python3 py/generator.py --seed 42 --chaos 5 --format lines`

## The two formats

`cars.json`, one car per line inside an array:

```json
[
    {"id": 1, "type": "GAS", "passengers": "PEOPLE", "isDining": true, "consumption": 39},
    {"id": 2, "type": "ELECTRIC", "passengers": "PEOPLE", "isDining": false, "consumption": 23}
]
```

`cars.txt`, one car per line, fields in the order `id type passengers isDining consumption`:

```text
1 GAS PEOPLE true 39
2 ELECTRIC PEOPLE false 23
```

Valid values: `type` is `ELECTRIC` or `GAS`, `passengers` is `PEOPLE` or `ROBOTS`,
`isDining` is `true` or `false`, and `consumption` is a whole number from 10 to 50.

## Broken cars (`--chaos`)

A broken car has exactly one of these problems:

| Problem | JSON | lines |
|---|---|---|
| unknown type | `"type": "HYDROGEN"` | `7 HYDROGEN PEOPLE false 50` |
| no passengers | `"passengers": null` | `9 GAS null true 20` |
| negative consumption | `"consumption": -31` | `4 GAS ROBOTS true -31` |
| missing field | no `isDining` key | `23 ELECTRIC PEOPLE 16` (4 words) |
| not a number | `"consumption": "lots"` | `8 ELECTRIC PEOPLE true lots` |

Your program must reject every broken car, log why, and keep going.

## stats.json

```json
{
    "SEED": 42,
    "TOTAL": 34,
    "REJECTED": 4,
    "ELECTRIC": 15,
    "GAS": 15,
    "PEOPLE": 15,
    "ROBOTS": 15,
    "DINING": 16,
    "NOT_DINING": 14,
    "DINING_BY": {"PEOPLE": 8, "ROBOTS": 8},
    "CONSUMPTION": {"ELECTRIC": 445, "GAS": 460}
}
```

All counts except `TOTAL` and `REJECTED` are for **valid cars only**. `PEOPLE` and `ROBOTS`
count every car with those passengers. `DINING_BY` counts only the ones that dined, which is
what your restaurants will see.
