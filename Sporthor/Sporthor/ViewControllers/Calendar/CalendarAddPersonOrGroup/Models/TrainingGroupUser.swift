//
//  TrainingGroupUser.swift
//  Sporthor
//
//  Created by derTurke on 28.06.2025.
//

import Foundation
import ModelParsers

struct TrainingGroupUser {
    @SafeDecode var groupId: String
    @LossyArray var users: [GetTrainingGroupUserModelUser]
}
