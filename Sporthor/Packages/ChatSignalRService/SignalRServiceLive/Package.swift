// swift-tools-version: 5.8
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "SignalRServiceLive",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "SignalRServiceLive",
            targets: ["SignalRServiceLive"]),
    ],
    dependencies: [
        .package(url: "https://github.com/moozzyk/SignalR-Client-Swift", from: "1.1.0"),
        .package(path: "../SignalRServiceKit"),
    ],
    targets: [
        .target(
            name: "SignalRServiceLive",
            dependencies: [
                .product(name: "SignalRClient", package: "SignalR-Client-Swift"),
                "SignalRServiceKit"
            ])
    ]
)
