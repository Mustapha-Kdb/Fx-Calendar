# 📅 UniPlan: Advanced JavaFX University Calendar Manager

UniPlan is a robust desktop application designed to simplify university schedule management. Built with **JavaFX**, it parses complex `.ics` files containing university-wide schedules and provides a personalized, secure, and highly interactive experience for students.

## ✨ Key Features

### 🔐 Secure & Personalized Access
* **Authentication System:** Secure login to protect user data and preferences.
* **Smart Default Loading:** Automatically detects and displays the calendar specific to the logged-in student's group upon startup.

### 🧭 Advanced Navigation & Customization
* **Global Access:** Beyond personal schedules, users can browse calendars for specific **Classes, Teachers, or Lecture Halls**.
* **Favorites System:** Users can save frequently visited calendars. Choices are persistent across sessions using a custom cookie-like mechanism.
* **Views:** Toggle seamlessly between **Daily, Weekly, and Monthly** views.

### 🔍 Deep Filtering & Versioning
* **Multi-Layer Filters:** Filter events by type (Exams, TPs, Lectures, etc.).
* **Calendar Versioning:** Switch between specific versions of the same schedule, such as **Group 1 vs Group 2** or **Classical vs Apprenticeship (Apprentissage)** tracks.

### 🖱️ Rich Interaction & UX
* **Contextual Details:** Hovering over a slot reveals detailed metadata: location, teacher, and session type.
* **Integrated Communication:** One-click redirection from a teacher's name to a pre-configured mailing portal.
* **Event Creation:** Students can manually add personal events to their schedule with full data persistence.
* **Modern UI:** A sleek interface following the University's official visual identity, featuring a persistent **Dark/Light Mode** toggle.

## 🏗️ Technical Architecture

* **Framework:** JavaFX (MVC Pattern)
* **Data Parsing:** Custom `.ics` (iCalendar) parser to handle massive university data files.
* **Persistence:** Implementation of a local storage system to maintain user themes, favorites, and custom events.
* **Styling:** CSS integration for modern UI components and dynamic theme switching.

## 🛠️ Installation & Setup
1. Clone the repository.
2. Ensure you have **Java 17+** and **JavaFX SDK** configured.
3. Import the project into your IDE (IntelliJ/Eclipse).
4. Run the `Main.java` file.

---
*Developed as part of the University Software Engineering curriculum, focusing on both backend logic and professional frontend design.*
