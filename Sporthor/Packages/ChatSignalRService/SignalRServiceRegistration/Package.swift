// swift-tools-version: 5.8
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "SignalRServiceRegistration",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "SignalRServiceRegistration",
            targets: ["SignalRServiceRegistration"]),
    ],
    dependencies: [
        .package(url: "https://github.com/hmlongco/Factory.git", from: "2.3.1"),
        .package(path: "../SignalRServiceKit"),
        .package(path: "../SignalRServiceLive")
    ],
    targets: [
        .target(
            name: "SignalRServiceRegistration",
            dependencies: [
                "SignalRServiceKit",
                "SignalRServiceLive",
                "Factory",
            ]
        )
    ]
)
