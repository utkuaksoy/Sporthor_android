//
//  SearchInteractor.swift
//  Sporthor
//
//  Created by derTurke on 7.03.2025.
//
//

import Foundation
import Factory

final class SearchInteractor: BaseInteractor {
    // MARK: - VIPER Variables
    weak var delegate: SearchInteractorDelegate? {
        get {
            return self.baseDelegate as? SearchInteractorDelegate
        }
        set {
            self.baseDelegate = newValue
        }
    }
    @LazyInjected(\.networkManager) private var networkManager
}

// MARK: - SearchInteractorProtocol
extension SearchInteractor: SearchInteractorProtocol {
    func search(_ request: [String : Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: SocialService.search(request), responseType: SearchesResponse.self, showLoading: false)
        switch result {
        case .success(let response):
            delegate?.didSearch(response.searchList)
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func addSearchHistory(_ request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: SocialService.addSearchHistory(request), responseType: NullResponse.self, showLoading: false)
        switch result {
        case .success(_):
            delegate?.didAddSearchHistory()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func getSearchHistory() async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: SocialService.getSearchHistory,
                                                  responseType: GetSearchHistoryResponse.self,
                                                  showLoading: false)
        switch result {
        case .success(let response):
            delegate?.didGetSearchHistory(response.histories ?? [])
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
    
    func removeSearchHistory(request: [String: Any]) async {
        guard let networkManager else { return }
        let result = await networkManager.request(service: SocialService.removeSearchHistory(request),
                                                  responseType: NullResponse.self,
                                                  showLoading: false)
        switch result {
        case .success(_):
            delegate?.didRemoveSearchHistory()
        case .failure(let error):
            delegate?.didFailure(error)
        }
    }
}
