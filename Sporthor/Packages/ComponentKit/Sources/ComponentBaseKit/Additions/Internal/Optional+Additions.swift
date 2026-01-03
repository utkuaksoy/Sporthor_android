//
//  Optional+Additions.swift
//  
//
//  Created by Mesut Canbaz on 10.02.2025.
//

import Foundation

extension Optional {

    func ifNil(_ value: @autoclosure () -> Wrapped) -> Wrapped {
        switch self {
        case .none:
            return value()
        case .some(let value):
            return value
        }
    }

    func forceIfExist<T>() -> T? {
        switch self {
        case .none:
            return nil
        case .some(let value):
            if let value = value as? T {
                return value
            } else {
                assertionFailure("\(value) should confirm \(T.self)")
                return .none
            }
        }
    }
}
