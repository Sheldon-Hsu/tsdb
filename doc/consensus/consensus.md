This document describes some of the things developers should agree on

## Data conversion

- String type data is stored in memory(TVList) as byte arrays
- Time types such as timestamp/date are converted to long storage immediately after being written
