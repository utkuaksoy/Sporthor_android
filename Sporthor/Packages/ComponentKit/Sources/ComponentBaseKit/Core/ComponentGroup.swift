//
//  ComponentGroup.swift
//  
//
//  Created by Mesut Canbaz on 10.02.2025.
//

import Foundation
import ModelParsers

public protocol ComponentGroup: ComponentItemGeneratorFromKind {
    associatedtype DisplayerDelegates
}
