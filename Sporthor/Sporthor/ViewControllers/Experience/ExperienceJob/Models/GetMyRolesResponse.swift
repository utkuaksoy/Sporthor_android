//
//  GetMyRolesResponse.swift
//  Sporthor
//
//  Created by derTurke on 8.07.2025.
//

import Foundation
import ModelParsers

struct GetMyRolesResponse: Decodable {
    @LossyArray var roles: [String]
}
