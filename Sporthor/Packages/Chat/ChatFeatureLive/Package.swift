// swift-tools-version: 5.8
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "ChatFeatureLive",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "ChatFeatureLive",
            targets: ["ChatFeatureLive"]),
    ],
    dependencies: [
        .package(path: "../ChatKit"),
        .package(url: "https://github.com/MessageKit/MessageKit.git", .upToNextMajor(from: "4.2.0")),
        .package(url: "https://github.com/hmlongco/Factory.git", from: "2.3.1"),
        .package(url: "https://github.com/hackiftekhar/IQKeyboardManager.git", from: "7.1.1"),
        .package(path: "../SignalRServiceKit"),
        .package(path: "../DesignKit"),
        .package(path: "../ComponentKit"),
        .package(path: "../UserKit"),
        .package(path: "../CommonKit"),
        .package(path: "../ThumbnailProviderKit"),
    ],
    targets: [
        .target(
            name: "ChatFeatureLive",
            dependencies: [
                "ChatKit",
                "MessageKit",
                "Factory",
                "SignalRServiceKit",
                "DesignKit",
                "ComponentKit",
                "UserKit",
                "CommonKit",
                "ThumbnailProviderKit",
                .product(name: "ChatCoordinator", package: "ChatKit"),
                .product(name: "IQKeyboardManagerSwift", package: "IQKeyboardManager")
            ])
    ]
)
