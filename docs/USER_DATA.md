# User Data and Persistence

This document outlines how user data, specifically their questionnaire results, will be stored and managed.

## 1. Core Principle: On-Device Storage

The application will prioritize user privacy by storing all data locally on the user's device. No cloud accounts or network connection will be required for the core functionality. This ensures that the sensitive, personal information from the questionnaire remains under the user's control.

## 2. Persistence Technology

- **Database for Results:** To store historical questionnaire results and enable future features like tracking trends over time, a `Room` database is the preferred solution. It provides a robust, structured way to store relational data locally.
- **User Identifier:** For analytics and crash reporting, a single, randomly generated UUID will be created for each installation. This anonymous identifier will be stored using `DataStore`. It helps in debugging and understanding usage patterns without compromising user identity.

## 3. Future Enhancements

While not in the initial scope, the following features are planned for future versions:

- **Export to JSON:** A feature will be added to allow users to export their results data as a JSON file. This empowers users to back up, analyze, or share their data on their own terms.
- **Cloud Accounts & Social Features:** Long-term possibilities include optional cloud-based accounts. This would enable features like sharing and comparing results with friends or partners and viewing historical trends across multiple devices. These features will be designed as opt-in and will be considered after the core on-device experience is fully realized.
