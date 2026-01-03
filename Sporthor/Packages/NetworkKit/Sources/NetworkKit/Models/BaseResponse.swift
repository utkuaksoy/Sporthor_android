//
//  BaseResponse.swift
//  NetworkKit
//
//  Created by Mesut Canbaz on 9.02.2025.
//

import Foundation

public struct BaseResponse<T: Decodable>: Decodable {
    public let status: String?
    public let message: String?
    public let isSuccess: Bool?
    public let error: BaseError?
    public let data: T?
    
    enum CodingKeys: CodingKey {
        case status
        case message
        case isSuccess
        case error
        case data
    }
    
    public init(status: String? = nil,
                message: String? = nil,
                isSuccess: Bool? = nil,
                error: BaseError? = nil,
                data: T? = nil) {
        self.status = status
        self.message = message
        self.isSuccess = isSuccess
        self.error = error
        self.data = data
    }
    
    public init(from decoder: any Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.status = try container.decodeIfPresent(String.self, forKey: .status)
        self.message = try container.decodeIfPresent(String.self, forKey: .message)
        self.isSuccess = try container.decodeIfPresent(Bool.self, forKey: .isSuccess)
        self.error = try container.decodeIfPresent(BaseError.self, forKey: .error)
        self.data = try container.decodeIfPresent(T.self, forKey: .data)
    }
}

public struct BaseError: Error, Decodable {
    public let isUserFriendly: Bool?
    public let message: String?
    public let code: Int?
    
    enum CodingKeys: CodingKey {
        case isUserFriendly
        case message
        case code
    }
    
    public init(isUserFriendly: Bool? = nil,
                message: String? = nil,
                code: Int? = nil) {
        self.isUserFriendly = isUserFriendly
        self.message = message
        self.code = code
    }
    
    public init(from decoder: any Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.isUserFriendly = try container.decodeIfPresent(Bool.self, forKey: .isUserFriendly)
        self.message = try container.decodeIfPresent(String.self, forKey: .message)
        self.code = try container.decodeIfPresent(Int.self, forKey: .code)
    }
}
