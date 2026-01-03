//
//  CollectionComponent.swift
//  
//
//  Created by Mesut Canbaz on 10.02.2025.
//

import UIKit

public protocol StaticComponent: Component where ViewModel: StaticComponentViewModel {

    var data: ViewModel.DataType { get }
}

public extension StaticComponent {

    func viewModel<T>(delegate: T?, defaultInsets: UIEdgeInsets) -> ViewModel {
        return ViewModel(data: self.data, defaultInsets: defaultInsets, delegate: delegate.forceIfExist())
    }
}
