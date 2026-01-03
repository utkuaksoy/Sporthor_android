//
//  NetworkLogger.swift
//  ChatKit
//
//  Created by Mesut Canbaz on 19.02.2025.
//

import Foundation

public protocol NetworkLogging {
    func logRequest(_ request: URLRequest)
    func logResponse(_ response: HTTPURLResponse?, data: Data?, error: Error?)
}

public final class NetworkLogger: NetworkLogging {
    public static let shared = NetworkLogger()
    private let isDebugMode: Bool
    
    public init(isDebugMode: Bool = true) {
        self.isDebugMode = isDebugMode
    }
    
    public func logRequest(_ request: URLRequest) {
        guard isDebugMode else { return }
        
        print("\n🚀 ====== Network Request ======")
        print("📍 URL: \(request.url?.absoluteString ?? "nil")")
        print("📝 Method: \(request.httpMethod ?? "nil")")
        print("📋 Headers: \(request.allHTTPHeaderFields ?? [:])")
        
        if let body = request.httpBody,
           let jsonString = String(data: body, encoding: .utf8) {
            print("📦 Body: \(jsonString)")
        }
    }
    
    public func logResponse(_ response: HTTPURLResponse?, data: Data?, error: Error?) {
        guard isDebugMode else { return }
        
        print("\n🔙 ====== Network Response ======")
        if let response = response {
            print("📍 URL: \(response.url?.absoluteString ?? "nil")")
            print("📊 Status Code: \(response.statusCode)")
            print("📋 Headers: \(response.allHeaderFields)")
        }
        
        if let data = data,
           let jsonString = try? JSONSerialization.jsonObject(with: data, options: .mutableContainers) {
            print("📦 Response Data: \(jsonString)")
        }
        
        if let error = error {
            print("❌ Error: \(error.localizedDescription)")
        }
        
        print("⏱ Response Time: \(Date())")
        print("=====================================\n")
    }
} 
