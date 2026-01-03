// swift-tools-version: 5.10

import PackageDescription

let package = Package(
    name: "ModelParsers",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "ModelParsers",
            targets: ["ModelParsers"]
        )
    ],
    dependencies: [],
    targets: [
        .target(
            name: "ModelParsers",
            dependencies: []
        )
    ]
)
