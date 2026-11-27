// Car generator for the Car Service lab. No libraries needed:
//
//     g++ -std=c++17 generator.cpp -o generator
//     ./generator                          # 30 cars, cars.json
//     ./generator --format lines           # 30 cars, cars.txt
//     ./generator --seed 42 --chaos 5      # same cars every time, 5 broken
//
// Writes the cars to one file (cars.json or cars.txt) and the expected
// statistics to stats.json. It never deletes anything.
// The same seed gives the same cars as generator.py.

#include <cstdint>
#include <ctime>
#include <fstream>
#include <iostream>
#include <map>
#include <sstream>
#include <string>
#include <utility>
#include <vector>

// simpler to type
typedef long long ll;
typedef std::uint64_t u64;

// SplitMix64: small, and written the same way in generator.py.
// If you are curious, this allows for the same seed number
// to have the same result. Think minecraft seeds.
// If you are truly curious:
// https://rosettacode.org/wiki/Pseudo-random_numbers/Splitmix64
class Random {
public:
  explicit Random(u64 seed) : state_(seed) {}

  u64 next() {
    auto z = (state_ += 0x9E3779B97F4A7C15ULL); // u64, like state_
    z = (z ^ (z >> 30)) * 0xBF58476D1CE4E5B9ULL;
    z = (z ^ (z >> 27)) * 0x94D049BB133111EBULL;
    return z ^ (z >> 31);
  }

  int below(int n) { return static_cast<int>(next() % static_cast<u64>(n)); }

private:
  u64 state_;
};

const char *CAR_TYPES[] = {"ELECTRIC", "GAS"};
const char *PASSENGER_TYPES[] = {"PEOPLE", "ROBOTS"};
const int CONSUMPTION_MIN = 10;
const int CONSUMPTION_MAX = 50;
const int CHAOS_KINDS = 5;

// One field, already written as JSON. Missing fields are not written.
struct Field {
  std::string json; // "\"GAS\"", "true", "42", "null"
  bool missing = false;

  static Field text(const std::string &s) { return {"\"" + s + "\""}; }
  static Field number(int n) { return {std::to_string(n)}; }
  static Field boolean(bool b) { return {b ? "true" : "false"}; }
  static Field null() { return {"null"}; }

  std::string word() const { // the same value for the lines format
    if (json.size() >= 2 && json.front() == '"')
      return json.substr(1, json.size() - 2);
    return json;
  }
};

struct Car {
  int id = 0;
  Field type, passengers, isDining, consumption;
  bool valid = true;
  // The real values, used for the stats of valid cars.
  std::string realType, realPassengers;
  bool realDining = false;
  int realConsumption = 0;
};

Car makeCar(Random &rng) {
  Car c;
  c.realType = CAR_TYPES[rng.below(2)];
  c.realPassengers = PASSENGER_TYPES[rng.below(2)];
  c.realDining = rng.below(2) == 0;
  c.realConsumption =
      CONSUMPTION_MIN + rng.below(CONSUMPTION_MAX - CONSUMPTION_MIN + 1);
  c.type = Field::text(c.realType);
  c.passengers = Field::text(c.realPassengers);
  c.isDining = Field::boolean(c.realDining);
  c.consumption = Field::number(c.realConsumption);
  return c;
}

Car breakCar(Car c, Random &rng) {
  c.valid = false;
  switch (rng.below(CHAOS_KINDS)) {
  case 0:
    c.type = Field::text("HYDROGEN");
    break;
  case 1:
    c.passengers = Field::null();
    break;
  case 2:
    c.consumption = Field::number(-c.realConsumption);
    break;
  case 3:
    c.isDining.missing = true;
    break;
  default:
    c.consumption = Field::text("lots");
    break;
  }
  return c;
}

std::vector<std::pair<std::string, const Field *>> fieldsOf(const Car &c,
                                                            const Field &id) {
  return {{"id", &id},
          {"type", &c.type},
          {"passengers", &c.passengers},
          {"isDining", &c.isDining},
          {"consumption", &c.consumption}};
}

std::string toJsonLine(const Car &c) {
  auto id = Field::number(c.id); // Field
  std::string out = "{";
  auto first = true;
  for (const auto &[name, field] : fieldsOf(c, id)) {
    if (field->missing)
      continue;
    if (!first)
      out += ", ";
    out += "\"" + name + "\": " + field->json;
    first = false;
  }
  return out + "}";
}

std::string toTextLine(const Car &c) {
  auto id = Field::number(c.id);
  std::string out;
  for (const auto &[name, field] : fieldsOf(c, id)) {
    if (field->missing)
      continue;
    if (!out.empty())
      out += " ";
    out += field->word();
  }
  return out;
}

void usage() {
  std::cout << "usage: generator [--count N] [--chaos N] [--seed N] [--format "
               "json|lines]\n"
               "  --count N   number of valid cars (default 30)\n"
               "  --chaos N   number of broken cars to add (default 0)\n"
               "  --seed N    same seed, same cars (default: current time)\n"
               "  --format    json: cars.json, lines: cars.txt with one car "
               "per line\n";
}

bool readNumber(const std::string &text, ll &out) {
  std::istringstream in(text);
  return (in >> out) && in.eof() && out >= 0;
}

int main(int argc, char *argv[]) {
  auto count = 30LL; // LL: ll, so readNumber can write into it
  auto chaos = 0LL;
  auto seed = static_cast<ll>(std::time(nullptr));
  std::string format = "json";

  for (auto i = 1; i < argc; ++i) {
    // Not auto here: `auto arg = argv[i];` would make arg a char*, and
    // arg == "--help" would compare two pointers, not the text.
    std::string arg = argv[i];
    if (arg == "--help" || arg == "-h") {
      usage();
      return 0;
    }
    if (i + 1 >= argc) {
      std::cerr << "missing value for " << arg << "\n";
      usage();
      return 2;
    }
    std::string value = argv[++i];
    auto ok = true;
    if (arg == "--count")
      ok = readNumber(value, count);
    else if (arg == "--chaos")
      ok = readNumber(value, chaos);
    else if (arg == "--seed")
      ok = readNumber(value, seed);
    else if (arg == "--format")
      ok = (format = value) == "json" || format == "lines";
    else
      ok = false;
    if (!ok) {
      std::cerr << "bad argument: " << arg << " " << value << "\n";
      usage();
      return 2;
    }
  }

  Random rng(static_cast<u64>(seed));
  std::vector<Car> cars;
  for (auto i = 0LL; i < count; ++i)
    cars.push_back(makeCar(rng));
  for (auto i = 0LL; i < chaos; ++i) {
    auto c = makeCar(rng); // Car, from the return type
    cars.push_back(breakCar(c, rng));
  }

  // Fisher-Yates shuffle, so the broken cars are mixed in.
  for (auto i = static_cast<int>(cars.size()) - 1; i > 0; --i) {
    auto j = rng.below(i + 1);
    std::swap(cars[i], cars[j]);
  }

  std::map<std::string, int> stats;
  for (std::size_t i = 0; i < cars.size(); ++i) {
    auto &c = cars[i]; // Car&
    c.id = static_cast<int>(i) + 1;
    if (!c.valid)
      continue;
    stats[c.realType]++;
    stats[c.realPassengers]++;
    if (c.realDining) {
      stats["DINING"]++;
      stats["DINING_" + c.realPassengers]++;
    } else {
      stats["NOT_DINING"]++;
    }
    stats["CONS_" + c.realType] += c.realConsumption;
  }

  std::string name = format == "json" ? "cars.json" : "cars.txt";
  std::ofstream carsFile(name, std::ios::binary);
  if (format == "json") {
    carsFile << "[\n";
    for (std::size_t i = 0; i < cars.size(); ++i)
      carsFile << "    " << toJsonLine(cars[i])
               << (i + 1 < cars.size() ? ",\n" : "\n");
    carsFile << "]\n";
  } else {
    for (const auto &c : cars)
      carsFile << toTextLine(c) << "\n";
  }

  std::ostringstream s;
  s << "{\n"
    << "    \"SEED\": " << seed << ",\n"
    << "    \"TOTAL\": " << cars.size() << ",\n"
    << "    \"REJECTED\": " << chaos << ",\n"
    << "    \"ELECTRIC\": " << stats["ELECTRIC"] << ",\n"
    << "    \"GAS\": " << stats["GAS"] << ",\n"
    << "    \"PEOPLE\": " << stats["PEOPLE"] << ",\n"
    << "    \"ROBOTS\": " << stats["ROBOTS"] << ",\n"
    << "    \"DINING\": " << stats["DINING"] << ",\n"
    << "    \"NOT_DINING\": " << stats["NOT_DINING"] << ",\n"
    << "    \"DINING_BY\": {\"PEOPLE\": " << stats["DINING_PEOPLE"]
    << ", \"ROBOTS\": " << stats["DINING_ROBOTS"] << "},\n"
    << "    \"CONSUMPTION\": {\"ELECTRIC\": " << stats["CONS_ELECTRIC"]
    << ", \"GAS\": " << stats["CONS_GAS"] << "}\n"
    << "}\n";

  std::ofstream statsFile("stats.json", std::ios::binary);
  statsFile << s.str();

  std::cout << "Wrote " << cars.size() << " cars to " << name << " (" << chaos
            << " broken), seed " << seed << "\n"
            << s.str();
  return 0;
}
