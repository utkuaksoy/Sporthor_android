//
//  LocationBuilder.swift
//  Sporthor
//
//  Created by derTurke on 7.05.2025.
//
//

import Foundation

final class LocationBuilder {
    static func build(delegate: LocationDelegate? = nil,
                      isDarkTheme: Bool = true,
                      isPlacemark: Bool = false) -> LocationViewController {
        let view = LocationViewController()
        let interactor = LocationInteractor()
        let router = LocationRouter(viewController: view)
        let presenter = LocationPresenter(view: view,
                                          interactor: interactor,
                                          router: router,
                                          delegate: delegate,
                                          isDarkTheme: isDarkTheme,
                                          isPlacemark: isPlacemark)
        view.presenter = presenter
        return view
    }
}
