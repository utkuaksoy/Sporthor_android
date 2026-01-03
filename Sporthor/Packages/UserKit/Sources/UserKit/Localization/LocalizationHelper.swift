//
//  LocalizationHelper.swift
//  Sporthor
//
//  Created by derTurke on 9.03.2025.
//

import Foundation
import NetworkKit
import CommonKit

public final class LocalizationHelper: Sendable {
    
    public static let shared = LocalizationHelper()
    
    public let userDefaultsKey = "LocalizationData"
    
    public func getLocalization() async -> Result<Bool, NetworkError> {
        let result = await NetworkManager().request(
            service: LocalizationService.getLocalization,
            responseType: LocalizationResponse.self
        )
        switch result {
        case .success(let response):
            saveToUserDefaults(localizations: response.localizations ?? [])
            return .success(true)
        case .failure(let error):
            return .failure(error)
        }
    }
    
    public func loadFromUserDefaults() -> [LocalizationItem] {
        return UserDefaultsManager.shared.get([LocalizationItem].self, forKey: userDefaultsKey) ?? []
    }
    
    public func saveToUserDefaults(localizations: [LocalizationItem]) {
        UserDefaultsManager.shared.set(localizations, forKey: userDefaultsKey)
    }
    
    public func localizedText(for key: String) -> String {
        let localizations = loadFromUserDefaults()
        return localizations.first(where: { $0.code == key })?.text ?? key
    }
}
