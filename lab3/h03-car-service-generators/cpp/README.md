# Generator - C++

One file, no libraries, no CMake. Any compiler with C++17 works.

## Build

Linux and macOS:

```bash
g++ -std=c++17 generator.cpp -o generator      # or: clang++ -std=c++17 ...
```

Windows (MinGW-w64):

```bash
g++ -std=c++17 generator.cpp -o generator.exe
```

Windows (Visual Studio, in the "Developer Command Prompt"):

```bash
cl /std:c++17 /EHsc generator.cpp
```

In an IDE (CLion, Visual Studio, Code::Blocks): create an empty C++ project, add
`generator.cpp`, and set the language standard to C++17.

## Run

Run it from the folder where your program will read the cars. It writes `cars.json` (or
`cars.txt`) and `stats.json` there.

```bash
./generator                              # 30 cars, cars.json
./generator --format lines               # 30 cars, cars.txt
./generator --seed 42 --chaos 5          # the same cars every time, 5 of them broken
./generator --help
```

All the options and file formats are in the [main README](../README.md).

## Problems?

- `'g++' is not recognized`: the compiler is not on your `PATH`. Restart the terminal after
  installing it, or use the full path to `g++.exe`.
- `error: ... structured bindings ...` or similar: your compiler defaults to an older
  standard. Check that `-std=c++17` (or `/std:c++17`) is there.
