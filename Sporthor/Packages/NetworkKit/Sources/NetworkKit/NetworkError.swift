//
//  NetworkError.swift
//  NetworkKit
//
//  Created by derTurke on 31.01.2025.
//

import Foundation

public enum NetworkError: Error {
    case badURL
    case badServerResponse
    case decodingError
    case noData
    case httpError(statusCode: Int)
    case unknown(Error)
    case error(BaseError)
    case unauthorized
    
    public var localizedDescription: String {
        switch self {
        case .badURL:
            return "Geçersiz URL"
        case .badServerResponse:
            return "Sunucu yanıtı geçersiz"
        case .decodingError:
            return "Veri çözümleme hatası"
        case .noData:
            return "Sunucudan veri alınamadı"
        case .httpError(let statusCode):
            return "HTTP Hatası: \(statusCode)"
        case .unknown(let error):
            return error.localizedDescription
        case .error(let baseError):
            return baseError.message ?? ""
        case .unauthorized:
            return "Unauthorized"
        }
    }
}
