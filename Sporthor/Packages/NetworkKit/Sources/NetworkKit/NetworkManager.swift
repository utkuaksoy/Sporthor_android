//
//  NetworkManager.swift
//  NetworkKit
//
//  Created by derTurke on 31.01.2025.
//

import Foundation
import CommonKit

public final class NetworkManager: NetworkKitProtocol {
    private let session: URLSession
    private let decoder: JSONDecoder
    private let logger: NetworkLogging
    private let helper: BaseHelper
    
    public init(
        session: URLSession = .shared,
        decoder: JSONDecoder = JSONDecoder(),
        logger: NetworkLogging = NetworkLogger.shared,
        helper: BaseHelper = BaseHelper.shared
    ) {
        self.session = session
        self.decoder = decoder
        self.logger = logger
        self.helper = helper
    }
    
    private func createRequest<T: NetworkService>(for service: T) -> URLRequest? {
        guard let url = URL(string: service.baseURL + service.path) else {
            return nil
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = service.method.rawValue
        request.allHTTPHeaderFields = service.headers
        request.httpBody = service.httpBody
        
        
        if let token = service.token?.trimmingCharacters(in: .whitespacesAndNewlines), !token.isEmpty {
            request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
        }
        
        var parameters = service.parameters ?? [:]
        let baseRequest = BaseRequest()
        let baseRequestDictionary = baseRequest.dictionary()
        
        for (key, value) in baseRequestDictionary ?? [:] {
            parameters[key] = value
        }
        
        
        if let parameters = service.parameters {
            switch service.method {
            case .POST, .PUT:
                do {
                    let bodyData = try JSONSerialization.data(withJSONObject: parameters, options: [])
                    request.httpBody = bodyData
                } catch {
                    return nil
                }
            case .GET:
                var urlComponents = URLComponents(url: url, resolvingAgainstBaseURL: false)
                urlComponents?.queryItems = parameters.map { URLQueryItem(name: $0.key, value: "\($0.value)") }
                if let finalURL = urlComponents?.url {
                    request.url = finalURL
                }
            default:
                break
            }
        }
        
        return request
    }
    
    public func request<T: NetworkService, R: Decodable>(
        service: T,
        responseType: R.Type,
        showLoading: Bool
    ) async -> Result<R, NetworkError> {
        if showLoading {
            await MainActor.run {
                BaseHelper.shared.showIndicator()
            }
        }
        guard let request = createRequest(for: service) else {
            await MainActor.run {
                BaseHelper.shared.hideIndicator()
            }
            return .failure(.badURL)
        }
        
        logger.logRequest(request)
        
        do {
            let (data, response) = try await session.data(for: request)
            
            guard let httpResponse = response as? HTTPURLResponse else {
                await MainActor.run {
                    BaseHelper.shared.hideIndicator()
                }
                return .failure(.badServerResponse)
            }
            
            if httpResponse.statusCode == 401 || httpResponse.statusCode == 403 {
                await MainActor.run {
                    BaseHelper.shared.hideIndicator()
                }
                return .failure(.unauthorized)
            }
            
            guard (200...299).contains(httpResponse.statusCode) else {
                await MainActor.run {
                    BaseHelper.shared.hideIndicator()
                }
                return .failure(.badServerResponse)
            }
            
            do {
                let baseResponse = try decoder.decode(BaseResponse<R>.self, from: data)
                guard let responseData = baseResponse.data else {
                    logger.logResponse(httpResponse,
                                       data: baseResponse.data as? Data,
                                       error: baseResponse.error)
                    await MainActor.run {
                        helper.hideIndicator()
                    }
                    return .failure(.error(baseResponse.error ?? BaseError()))
                }
                logger.logResponse(httpResponse, data: responseData as? Data, error: nil)
                await MainActor.run {
                    BaseHelper.shared.hideIndicator()
                }
                return .success(responseData)
            } catch {
                logger.logResponse(httpResponse, data: data, error: error)
                await MainActor.run {
                    BaseHelper.shared.hideIndicator()
                }
                return .failure(.decodingError)
            }
            
        } catch {
            logger.logResponse(nil, data: nil, error: error)
            await MainActor.run {
                BaseHelper.shared.hideIndicator()
            }
            return .failure(.unknown(error))
        }
    }
}
