// swift-tools-version: 5.8
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "ChatRegistration",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "ChatRegistration",
            targets: ["ChatRegistration"]),
    ],
    dependencies: [
        .package(url: "https://github.com/hmlongco/Factory.git", from: "2.3.1"),
        .package(path: "../ChatKit"),
        .package(path: "../ChatFeatureLive")
    ],
    targets: [
        .target(
            name: "ChatRegistration",
            dependencies: [
                "ChatKit",
                "ChatFeatureLive",
                "Factory",
                .product(name: "ChatCoordinator", package: "ChatKit"),
            ]
        )
    ]
)
