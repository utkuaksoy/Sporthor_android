//
//  EmptyViewDataModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.03.2025.
//

import ComponentBaseKit
import Foundation
import ModelParsers

struct EmptyViewDataModel: Decodable {
    @SafeDecode
    private(set) var title: String
    @SafeDecode
    private(set) var emptyImage: String
}
