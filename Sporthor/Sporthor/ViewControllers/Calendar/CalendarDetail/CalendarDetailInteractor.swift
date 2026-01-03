//
//  CalendarDetailInteractor.swift
//  Sporthor
//
//  Created by derTurke on 21.05.2025.
//
//

import Foundation
import Factory

final class CalendarDetailInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: CalendarDetailInteractorDelegate? {
        get {
            return self.baseDelegate as? CalendarDetailInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - CalendarDetailInteractorProtocol
extension CalendarDetailInteractor: CalendarDetailInteractorProtocol {
    func getCalendarDetail(_ date: String) async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: CalendarService.getCalendarDetail(date), responseType: GetCalendarDetailResponse.self, showLoading: false)
        
        switch result {
        case .success(let response):
            delegate?.didGetCalendarDetail(response.tasks)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
