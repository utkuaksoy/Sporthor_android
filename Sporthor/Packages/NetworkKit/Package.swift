// swift-tools-version: 5.8
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "NetworkKit",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "NetworkKit",
            targets: ["NetworkKit"]),
    ],
    dependencies: [
        .package(path: "../CommonKit"),
        .package(url: "https://github.com/hmlongco/Factory.git", from: "2.3.1"),
    ],
    targets: [
        .target(
            name: "NetworkKit",
            dependencies: [
                "CommonKit",
                "Factory",
            ]
        ),
    ]
)
