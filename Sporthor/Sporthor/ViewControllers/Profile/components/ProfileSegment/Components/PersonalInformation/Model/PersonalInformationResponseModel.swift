//
//  PersonalInformationResponseModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import ComponentBaseKit
import Foundation
import ModelParsers

struct PersonalInformationResponseModel: Decodable {
    @SafeDecode
    private(set) var personalId: String
    @LossyArray
    private(set) var components: [PersonalInformationComponentModel]
}
