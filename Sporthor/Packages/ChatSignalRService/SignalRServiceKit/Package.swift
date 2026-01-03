// swift-tools-version: 5.8
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "SignalRServiceKit",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "SignalRServiceKit",
            targets: ["SignalRServiceKit"]),
    ],
    dependencies: [
        .package(url: "https://github.com/moozzyk/SignalR-Client-Swift", from: "1.1.0"),
        .package(url: "https://github.com/hmlongco/Factory.git", from: "2.3.1")
    ],
    targets: [
        .target(
            name: "SignalRServiceKit",
            dependencies: [
                "Factory",
                .product(name: "SignalRClient", package: "SignalR-Client-Swift")
            ])
    ]
) 
