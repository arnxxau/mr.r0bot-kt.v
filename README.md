# mr.r0bot-kt.v ![GitHub release (latest by date including pre-releases)](https://img.shields.io/github/v/release/arnxxau/mr.r0bot-kt.v?include_prereleases)
Mr.r0bot-kt.v is tool that handles computer logic calculations and provides a better experience through your stay at "IC".

## Modules
At the moment, only two modules are available, and none of them are 100% completed.
### ALU MODULE ; Handles simple ALU (Arithmetic Logic Uni) calculations.
 Currently, only one functionality is supported:  
 - Mnemonic to control phrase

(i.g) -> INPUT: `ADD R1, R2, R3`
OUTPUT:
````
┌────────┬────────┬────────┬────────┬────────┬────────┬────────┬───────┬───────┐
│@A      │@B      │Rb/N    │OP      │F       │In/Alu  │@D      │WrD    │N      │
├────────┼────────┼────────┼────────┼────────┼────────┼────────┼───────┼───────┤
│010     │011     │1       │00      │100     │0       │001     │1      │XXXX   │
└────────┴────────┴────────┴────────┴────────┴────────┴────────┴───────┴───────┘
Binary Chain: 010011100100000110000000000000000
Hexa Chain: 0x9C830000
````

### SISA-I MODULE ; Handles all types of calculations regarding SISC Harvard unicycle computer
Current supported functionalities:
- Hex to assembly
- Binary to assembly
- Assembly to hex
- Assembly to control phrase

Hex to assembly (i.e) -> INPUT: `0x1559`
OUTPUT:
````
CMPLE R3, R2, R5
````

Assembly to hex (i.e) -> INPUT: `ADDI R5, R7, 0x20`
OUTPUT:
````
0x2F60
````
Assembly to control phrase(i.e) -> INPUT: `STB -3(R4), R7`
OUTPUT:
````
┌─────┬─────┬─────┬─────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┬────┐
│@A   │@B   │Rb/N │OP   │F   │-/i/│@D  │WrD │Wr-O│Rd-I│Wr-M│Byte│TknB│N   │ADDR│
│     │     │     │     │    │l/a │    │    │ut  │n   │em  │    │r   │    │-IO │
├─────┼─────┼─────┼─────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┼────┤
│100  │111  │0    │00   │100 │XX  │XXX │0   │0   │0   │1   │1   │0   │FFFD│XX  │
└─────┴─────┴─────┴─────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┴────┘
Binary Chain: 10011100010000000000110111111111111110100000000
Hexa Chain: 0x4E2006FFFD00
````
## Thanks for using mr.r0bot-kt.v!
![Alt Text](https://sinordeniconcierto.files.wordpress.com/2017/01/robot11.gif?w=420&h=236)