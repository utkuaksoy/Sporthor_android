// swift-tools-version: 5.8
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "DesignKit",
    defaultLocalization: "tr",
    platforms: [
            .iOS(.v14)
        ],
    products: [
        .library(
            name: "DesignKit",
            targets: ["DesignKit"]),
    ],
    dependencies: [
        .package(url: "https://github.com/SwiftGen/SwiftGen.git", from: "6.6.3")
    ],
    targets: [
        .target(
            name: "DesignKit",
            dependencies: [],
            resources: [
                .process("Resources/Fonts")
            ]
        )
    ]
)
