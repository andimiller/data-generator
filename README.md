# data-generator

This is a simple framework for scala-native that makes a program that will generate arbitrary instances of a type to stdout, using `Arbitrary` from scalacheck and `Encoder` from circe.

## Usage

```
Usage:
    your-generator stream
    your-generator generate

A generator program to generate data

Options and flags:
    --help
        Display this help text.

Subcommands:
    stream
        Generate an infinite stream of items
    generate
        Generate a set number of items
```

### Stream

```
Usage: your-generator stream [--every <duration>] [--seed <integer>]

Generate an infinite stream of items

Options and flags:
    --help
        Display this help text.
    --every <duration>
        How often to generate an item, default 1 second
    --seed <integer>
        Seed for the random number generator
```

### Generate

```
Usage: your-generator generate [--seed <integer>] <count>

Generate a set number of items

Options and flags:
    --help
        Display this help text.
    --seed <integer>
        Seed for the random number generator
```

## License

This project is licensed under the MIT License
