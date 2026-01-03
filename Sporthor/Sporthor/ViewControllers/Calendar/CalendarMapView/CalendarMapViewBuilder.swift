//
//  CalendarMapViewBuilder.swift
//  Sporthor
//
//  Created by derTurke on 9.06.2025.
//
//

import Foundation
import MapKit

final class CalendarMapViewBuilder {
    static func build(delegate: CalendarMapViewDelegate? = nil,
                      placemark: CLPlacemark? = nil) -> CalendarMapViewViewController {
        let view = CalendarMapViewViewController()
        let interactor = CalendarMapViewInteractor()
        let router = CalendarMapViewRouter(viewController: view)
        let presenter = CalendarMapViewPresenter(view: view,
                                                 interactor: interactor,
                                                 router: router,
                                                 delegate: delegate,
                                                 placemark: placemark)
        view.presenter = presenter
        return view
    }
}
