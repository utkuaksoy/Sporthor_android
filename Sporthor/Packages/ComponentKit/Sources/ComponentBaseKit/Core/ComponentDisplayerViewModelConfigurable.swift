//
//  ComponentDisplayerViewModelConfigurable.swift
//  
//
//  Created by Mesut Canbaz on 10.02.2025.
//

import UIKit

public protocol ComponentDisplayerViewModelConfigurable {
    associatedtype Delegate
    associatedtype ViewModel

    func configure(with viewModel: ViewModel, at indexPath: IndexPath, delegate: Delegate?)
}
