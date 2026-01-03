//
//  Array+Additions.swift
//  Sporthor
//
//  Created by Mesut on 11.02.2025.
//

import Foundation

extension Array {
    subscript(safe index: Int) -> Element? {
        return indices.contains(index) ? self[index] : nil
    }
}
