// swift-tools-version: 5.8
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "CommonKit",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "CommonKit",
            targets: ["CommonKit"]),
    ],
    dependencies: [
        .package(path: "../DesignKit"),
        .package(url: "https://github.com/hmlongco/Factory.git", from: "2.3.1")
    ],
    targets: [
        .target(
            name: "CommonKit",
            dependencies: [
                "DesignKit",
                "Factory"
            ]
        ),
    ]
)

