//
//  HeaderModel.swift
//  Sporthor
//
//  Created by derTurke on 27.03.2025.
//

import Foundation

struct HeaderModel: Codable, Hashable, Equatable {
    var headerIcon: Icons
    var icons: [Icons]?
}
