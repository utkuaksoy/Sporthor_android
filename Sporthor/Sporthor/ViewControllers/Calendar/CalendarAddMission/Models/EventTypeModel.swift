//
//  EventTypeModel.swift
//  Sporthor
//
//  Created by derTurke on 26.05.2025.
//

import Foundation
import ModelParsers

struct EventTypeResponse: Decodable {
    @LossyArray var types: [EventTypeModel]
}

struct EventTypeDetailResponse: Decodable {
    var detail: EventTypeModel?
}

struct EventTypeModel: Decodable {
    @SafeDecode var name: String
    @SafeDecode var value: String
    @SafeDecode var detail: String
    @SafeDecode var isSelected: Bool
    
    init(name: String = "",
         value: String = "",
         detail: String = "",
         isSelected: Bool = false) {
        self.name = name
        self.value = value
        self.detail = detail
        self.isSelected = isSelected
    }
}
