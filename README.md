# Trade Store Application

## Main Components : 

- Producers :  Two Trade producers threads producing the different trades pushing the them in blocking queue.
- Consumer: Trade Consumer thread pull the trade info object from blocking queue and stores it into Store object.
- Trade Store -  Store object backed by Map & Set to cater the business logic.

## Program Input 

| Trade Id | Version | Counter Party | BookId | Created Date | Maturity Date | Expired |
| ------ | ------ |------ |------ |------ |------ |------ |
| T1 | 10 | C1 | B1 | 01-Jan-2021 | 01-Jan-2022 | N |
| T2 | 1 | C2 | B2 | 20-Jun-2021 | 01-Jan-2022 | N |
| T2 | 1 | C2 | B2 | 20-Jun-2021 | 10-Oct-2021 | N |
| T3 | 3 | C1 | B1 | 01-Jan-2021 | 01-Jan-2022 | N |
| T3 | 1 | C1 | B1 | 01-Jan-2021 | 01-Jan-2022 | N |
| T4 | 1 | C2 | B2 | 20-Jun-2015 | 10-Oct-2016 | N |
| T5 | 1 | C2 | B2 | 20-Jun-2021 | Current Date (23-Sept-2021) | N |
| T1 | 1 | C1 | B1 | 01-Jan-2021 | 01-Jan-2022 | N |

## Store Snapshot After Program Execution 

| Trade Id | Version | Counter Party | BookId | Created Date | Maturity Date | Expired |
| ------ | ------ |------ |------ |------ |------ |------ |
| T1 | 10 | C1 | B1 | 01-Jan-2021 | 01-Jan-2022 | N |
| ~~T2~~ | ~~1~~ | ~~C2~~ | ~~B2~~ | ~~20-Jun-2021~~ | ~~01-Jan-2022~~ | ~~N~~ |
| T2 | 1 | C2 | B2 | 20-Jun-2021 | 10-Oct-2021 | N |
| T3 | 3 | C1 | B1 | 01-Jan-2021 | 01-Jan-2022 | N |
| ~~T3~~ | ~~1~~ | ~~C1~~ | ~~B1~~ | ~~01-Jan-2021~~ | ~~01-Jan-2022~~ | ~~N~~ |
| ~~T4~~ | ~~1~~ | ~~C2~~ | ~~B2~~ | ~~20-Jun-2015~~ | ~~10-Oct-2016~~ | ~~N~~ |
| T5 | 1 | C2 | B2 | 20-Jun-2021 | Current Date (23-Sept-2021) | Y |
| ~~T1~~ | ~~1~~ | ~~C1~~ | ~~B1~~ | ~~01-Jan-2021~~ | ~~01-Jan-2022~~ | ~~N~~ |

