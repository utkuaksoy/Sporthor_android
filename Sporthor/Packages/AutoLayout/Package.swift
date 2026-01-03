// swift-tools-version: 5.10

import PackageDescription

let package = Package(
    name: "AutoLayout",
    platforms: [.iOS(.v13)],
    products: [
        .library(
            name: "AutoLayout",
            targets: ["AutoLayout"]
        )
    ],
    dependencies: [],
    targets: [
        .target(
            name: "AutoLayout",
            dependencies: []
        )
    ]
)
