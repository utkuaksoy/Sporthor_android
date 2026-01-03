// The Swift Programming Language
// https://docs.swift.org/swift-book

import Foundation
import Factory

public protocol NetworkKitProtocol {
    func request<T: NetworkService, R: Decodable>(
        service: T,
        responseType: R.Type,
        showLoading: Bool
    ) async -> Result<R, NetworkError>
}

public extension NetworkKitProtocol {
    func request<T: NetworkService, R: Decodable>(
        service: T,
        responseType: R.Type
    ) async -> Result<R, NetworkError> {
        return await request(service: service, responseType: responseType, showLoading: true)
    }
}

public extension Container {
    var networkManager: Factory<NetworkKitProtocol?> {
        self { nil }.singleton
    }
}

public struct NetworkKitRegistration {
    public static func registerLive(session: URLSession = .shared,
                                    decoder: JSONDecoder = JSONDecoder()) {
        Container.shared.networkManager.register {
            NetworkManager(session: session,
                           decoder: decoder)
        }
    }
}
