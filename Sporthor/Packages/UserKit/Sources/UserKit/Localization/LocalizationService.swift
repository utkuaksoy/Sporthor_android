//
//  LocalizationService.swift
//  UserKit
//
//  Created by derTurke on 9.03.2025.
//

import Foundation
import NetworkKit

enum LocalizationService {
    case getLocalization
}

extension LocalizationService: NetworkService {
    var path: String {
        switch self {
        case .getLocalization:
            return "/api/Configuration/GetLocalization"
        }
    }
    
    var method: NetworkKit.HTTPMethod {
        .GET
    }
    
    var parameters: [String : Any]? {
        return nil
    }
}
