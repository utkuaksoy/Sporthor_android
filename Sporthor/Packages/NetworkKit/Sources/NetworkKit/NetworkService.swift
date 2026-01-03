//
//  NetworkService.swift
//  NetworkKit
//
//  Created by derTurke on 31.01.2025.
//

import Foundation
import CommonKit

public enum HTTPMethod: String {
    case GET
    case POST
    case PUT
    case DELETE
    case PATCH
}

public protocol NetworkService {
    var baseURL: String { get }
    var path: String { get }
    var method: HTTPMethod { get }
    var headers: [String: String]? { get }
    var parameters: [String: Any]? { get }
    var httpBody: Data? { get }
    var token: String? { get }
}

public extension NetworkService {
    var baseURL: String {
        return NetworkConstants.baseURL
    }
    
    var headers: [String: String]? {
        return ["accept": "text/plain",
                "Content-Type": "application/json",
                "Accept-Language": UserDefaultsManager.shared.getString(forKey: "lang") ?? "tr_TR"]
    }
    
    var token: String? {
        return nil
    }
    
    var parameters: [String: Any]? {
        return nil
    }
    
    var httpBody: Data? {
        return nil
    }
}
