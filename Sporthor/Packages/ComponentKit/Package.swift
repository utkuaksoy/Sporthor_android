// swift-tools-version: 5.8
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "ComponentKit",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "ComponentKit",
            targets: ["ComponentKit"]),
        .library(
            name: "BarVisibilityKit",
            targets: ["BarVisibilityKit"]),
        .library(
            name: "ComponentBaseKit",
            targets: ["ComponentBaseKit"])
    ],
    dependencies: [
        .package(path: "../DesignKit"),
        .package(url: "https://github.com/hmlongco/Factory.git", from: "2.3.1"),
        .package(url: "https://github.com/onevcat/Kingfisher.git", from: "8.0.0"),
        .package(url: "https://github.com/slackhq/PanModal.git", from: "1.2.6"),
        .package(path: "../ModelParsers")
    ],
    targets: [
        .target(
            name: "ComponentKit",
            dependencies: [
                "DesignKit",
                "Factory",
                "Kingfisher",
                "PanModal"
            ]
        ),
        .target(name: "BarVisibilityKit"),
        .target(
            name: "ComponentBaseKit",
            dependencies: [
                .product(name: "ModelParsers", package: "ModelParsers"),
            ]
        )
    ]
)
