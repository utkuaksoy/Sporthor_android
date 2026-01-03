//
//  GetBlockUserResponse.swift
//  Sporthor
//
//  Created by GÜRHAN YUVARLAK on 16.08.2025.
//

import Foundation
import ModelParsers

struct GetBlockUserResponse: Decodable {
    @LossyArray var users: [GetTrainingGroupUserModelUser]
}
