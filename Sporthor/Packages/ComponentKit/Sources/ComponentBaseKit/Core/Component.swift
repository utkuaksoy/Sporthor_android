//
//  Component.swift
//  
//
//  Created by Mesut Canbaz on 10.02.2025.
//

import UIKit

public protocol Component: Decodable {
    associatedtype ViewModel
    func viewModel<T>(delegate: T?, defaultInsets: UIEdgeInsets) -> ViewModel
}
