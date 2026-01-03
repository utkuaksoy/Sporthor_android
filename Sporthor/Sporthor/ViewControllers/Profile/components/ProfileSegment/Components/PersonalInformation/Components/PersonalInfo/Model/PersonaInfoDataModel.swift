//
//  PersonaInformationDataModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import Foundation
import ModelParsers

struct PersonaInfoDataModel: Decodable {
    @SafeDecode
    var imageUrl: String
    @SafeDecode
    var name: String
    let nationalityName: String?
    let flagIcon: String?
    let birthDate: String?
    let height: String?
    let weight: String?
}
