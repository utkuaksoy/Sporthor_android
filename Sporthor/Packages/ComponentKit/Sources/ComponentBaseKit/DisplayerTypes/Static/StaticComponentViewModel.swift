//
//  CollectionComponentViewModel.swift
//  
//
//  Created by Mesut Canbaz on 10.02.2025.
//

import UIKit

public protocol StaticComponentViewModel: ComponentViewModel {

    var hideOnScroll: Bool { get }

    func view(delegate: AnyObject?) -> UIView & ComponentDisplayer
}

public extension StaticComponentViewModel {

    var isSelectable: Bool { false }
}
