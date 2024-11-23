# GrabJob Mobile App

## Overview
GrabJob Mobile is an innovative Android application designed to bridge the gap between clients and skilled workers. The platform enables clients to easily find and connect with qualified professionals across various trades and skills, while providing workers with opportunities to showcase their expertise and find work.

## Features
- **Skilled Worker Discovery**
  - Advanced search filters by trade, skill level, and location
  - Detailed worker profiles with portfolios and work history
  - Real-time availability status
  - Rating and review system

- **Client Tools**
  - Quick worker search and booking
  - Project posting and management
  - Direct messaging with workers
  - Payment integration
  - Work history tracking

- **Worker Features**
  - Professional profile creation
  - Skill and certification verification
  - Work schedule management
  - Job acceptance and tracking
  - Payment receipt and history

- **Platform Features**
  - Real-time chat and notifications
  - Location-based matching
  - Secure payment processing
  - Rating and feedback system
  - Work completion verification

## Technical Specifications

### Development Environment
- Android Studio
- Kotlin Programming Language
- Minimum SDK Version: 24 (Android 7.0)
- Target SDK Version: 34 (Android 14)

### Design System
#### Typography
The app uses a carefully selected typography system:
- **Primary Font**: Roboto
  - Used for all general text content
  - Provides excellent readability
  - Weights: Regular, Bold, Italic

- **Secondary Font**: Poppins
  - Used for headings and emphasis
  - Creates visual hierarchy
  - Weights: Regular, Bold, Italic

### Project Structure
```
app/
├── src/
│   ├── main/
│   │   ├── java/         # Kotlin/Java source files
│   │   ├── res/
│   │   │   ├── font/     # Custom fonts
│   │   │   ├── layout/   # UI layout files
│   │   │   ├── values/   # Resources (colors, strings, etc.)
│   │   │   └── drawable/ # Images and icons
│   │   └── AndroidManifest.xml
│   └── test/             # Unit tests
└── build.gradle         # App-level build configuration
```

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or newer
- JDK 11 or newer
- Android SDK

### Installation
1. Clone the repository:
```bash
git clone https://github.com/yourusername/GrabJob-Mobile.git
```

2. Open the project in Android Studio

3. Sync project with Gradle files

4. Run the app on an emulator or physical device

## Building and Running
- Debug build: `./gradlew assembleDebug`
- Release build: `./gradlew assembleRelease`

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/NewFeature`)
3. Commit your changes (`git commit -m 'Add some NewFeature'`)
4. Push to the branch (`git push origin feature/NewFeature`)
5. Open a Pull Request

## Security and Privacy
- Secure user authentication
- Encrypted messaging
- Protected payment processing
- Personal data protection
- Location data security

## Support
For support, email: support@grabjob.com

## License
Licensed under the [MIT License](LICENSE)

## Acknowledgments
- Google Fonts for Roboto and Poppins fonts
- Firebase for real-time features
- Google Maps for location services
- Stripe for payment processing
