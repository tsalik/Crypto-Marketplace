## Overview
This project implements a crypto marketplace. The project focuses on demonstrating the core functionality and approach to problem-solving.

## Architecture and Code Structure
The project follows the **MVVM** pattern for Android, which promotes separation of concerns, making the codebase more testable and scalable without over-engineering.

- **MVVM Architecture**: Core logic and UI interactions are managed in the ViewModel, while the UI remains focused on rendering the data provided.
- **Feature Modules**: The code is organized into multiple feature modules, keeping related functionality encapsulated and improving modularity.
- **Design System Module**: A separate module houses the design system, maintaining consistency across the app’s UI. Some reusable components (such as `ErrorScreen`) could be moved here for better organization and reusability.

This structure provides a balance between maintainability and simplicity, ensuring a solid foundation without unnecessary complexity.

## Future Improvements
If given more time, I would focus on the following enhancements to improve the overall quality, performance, and user experience of the app:

1. **Error Handling & Edge Cases**
    - Add more comprehensive error handling for network and parsing errors to provide users with meaningful feedback if data retrieval fails.
    - Handle additional edge cases, like unexpected input values or incomplete data responses, to ensure app stability.

2. **UI/UX Enhancements**
    - Improve the UI by adding animations for smoother transitions, especially in areas like loading and price changes.
    - Use the [config endpoint](https://docs.bitfinex.com/reference/rest-public-conf) to fetch friendlier names of the tickers and show these along with the ticker
    - Allow users to filter both based on ticker and friendly name

3. **Testing**
    - Add UI tests to cover major functionalities and verify that critical flows work as expected.
    - Add screenshot tests

4. **Feature Enhancements**
   - Allow users to change between USD, EUR and other fiat currencies

Each of these improvements would help make the app more robust, user-friendly, and maintainable.
