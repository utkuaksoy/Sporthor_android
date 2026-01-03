// swift-tools-version: 5.8
import PackageDescription

let package = Package(
    name: "ChatKit",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "ChatKit",
            targets: ["ChatKit"]),
        .library(
            name: "ChatCoordinator",
            targets: ["ChatCoordinator"]),
    ],
    dependencies: [
        .package(url: "https://github.com/hmlongco/Factory.git", from: "2.3.1"),
        .package(url: "https://github.com/MessageKit/MessageKit.git", .upToNextMinor(from: "4.2.0")),
        .package(path: "../ModelParsers")
    ],
    targets: [
        .target(
            name: "ChatKit",
            dependencies: [
                "Factory",
                "MessageKit",
                .product(name: "ModelParsers", package: "ModelParsers"),
            ]
        ),
        .target(
            name: "ChatCoordinator",
            dependencies: [
                "ChatKit",
                "Factory"
            ]
        )
    ]
)
