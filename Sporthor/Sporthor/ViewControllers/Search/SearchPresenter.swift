//
//  SearchPresenter.swift
//  Sporthor
//
//  Created by derTurke on 7.03.2025.
//
//

import Foundation

final class SearchPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: SearchPresenterDelegate? {
        get { return self.baseView as? SearchPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: SearchInteractorProtocol {
        get { return self.baseInteractor as! SearchInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: SearchRouterProtocol {
        get { return self.baseRouter as! SearchRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: SearchPresenterDelegate, interactor: SearchInteractorProtocol, router: SearchRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    var searchState: SearchState = .past
    var pastSearches: [SearchHistory] = []
    var searches: [SearchList] = []
    var searchWorkItem: DispatchWorkItem?
    var discovers: [String] = []
    lazy var searchText: String = "" {
        didSet {
            view?.reloadData()
        }
    }
    private var selectedIndexPath: IndexPath?
    private var isSelectedSearch: Bool = false
    private var selectedPastSearch: SearchHistory?
}

// MARK: - SearchPresenterProtocol
extension SearchPresenter: SearchPresenterProtocol {
    func viewDidLoad() {
        view?.prepareNavigationBar()
        view?.prepareUI()
        prepareDiscover()
    }
    
    private func navigate(_ route: SearchRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(route)
        }
    }
    
    private func prepareDiscover() {
        discovers = [
            Asset.discover1.name,
            Asset.discover2.name,
            Asset.discover3.name,
            Asset.discover2.name,
            Asset.discover3.name,
            Asset.discover1.name,
            Asset.discover3.name,
            Asset.discover2.name,
            Asset.discover1.name,
            Asset.discover3.name,
            Asset.discover1.name,
            Asset.discover2.name,
            Asset.discover3.name
        ]
    }
    
    private func updateSearchState(to newState: SearchState) {
        searchState = newState
        view?.reloadData()
    }
    
    func searchBegin() {
        guard searchText.isEmpty else { return }
        getSearchHistory()
    }
    
    private func getSearchHistory() {
        Task {
            @MainActor in
            await interactor.getSearchHistory()
        }
    }
    
    func search(_ text: String) {
        self.searchText = text
        searchWorkItem?.cancel()
        
        guard searchState != .discover, !text.isEmpty else {
            getSearchHistory()
            return
        }
        
        let workItem = DispatchWorkItem { [weak self] in
            guard let self = self else { return }
            let request: [String: Any] = ["searchTerm": text.localizedLowercase]
            Task {
                @MainActor in
                await self.interactor.search(request)
            }
        }
        
        searchWorkItem = workItem
        DispatchQueue.global().asyncAfter(deadline: .now() + 0.5, execute: workItem)
    }
    
    func searchCancel() {
        searchWorkItem?.cancel()
        self.updateSearchState(to: .past)
    }
    
    func searchEnd(_ text: String) {
        searchWorkItem?.cancel()
        guard searchState == .search,
              !text.isEmpty,
              !isSelectedSearch else {
            isSelectedSearch = false
            return
        }
        addSearchHistory(request: ["searchTerm": text])
    }
    
    private func addSearchHistory(request: [String: Any]) {
        Task {
            @MainActor in
            await interactor.addSearchHistory(request)
        }
    }
    
    func didSelectItemAt(_ indexPath: IndexPath) {
        selectedIndexPath = indexPath
        switch searchState {
        case .search:
            guard let type = searches[safe: indexPath.row]?.type,
                  let id = searches[safe: indexPath.row]?.id,
                  indexPath.section != 0 else { return }
            isSelectedSearch = true
            switch SearchType(rawValue: type) {
            case .user:
                addSearchHistory(request: ["userId": id])
            case .team:
                addSearchHistory(request: ["teamId": id])
            default:
                break
            }
        case .past:
            guard let searchUser = pastSearches[indexPath.section].searchUser,
                  let username = searchUser.userName,
                  let userId = pastSearches[indexPath.section].searchUser?.userId else {
                let searchQuery = pastSearches[indexPath.section].searchQuery ?? ""
                view?.didChangeSearchBarText(searchQuery)
                search(searchQuery)
                return
            }
            navigate(.profile(id: userId, username: username))
        default:
            break
        }
    }
    
    func deletePastItemAt(model: SearchHistory) {
        self.selectedPastSearch = model
        guard let id = model.id else { return }
        removeSearchHistory(request: ["id": id])
    }
    
    func deleteAllPastItem() {
        self.selectedPastSearch = nil
        removeSearchHistory(request: [:])
    }
    
    private func removeSearchHistory(request: [String: Any]) {
        Task {
            @MainActor in
            await interactor.removeSearchHistory(request: request)
        }
    }
}

// MARK: - SearchInteractorDelegate
extension SearchPresenter: SearchInteractorDelegate {
    func didSearch(_ searchList: [SearchList]) {
        self.searches = searchList
        self.updateSearchState(to: .search)
    }
    
    func didAddSearchHistory() {
        guard let selectedIndexPath,
              let id = searches[safe: selectedIndexPath.row]?.id,
              let username = searches[safe: selectedIndexPath.row]?.userName else { return }
        navigate(.profile(id: id, username: username))
    }
    
    func didGetSearchHistory(_ histories: [SearchHistory]) {
        self.pastSearches = histories
        updateSearchState(to: .past)
    }
    
    func didRemoveSearchHistory() {
        if let selectedPastSearch,
            let id = selectedPastSearch.id {
            self.pastSearches.removeAll { $0.id == id }
        } else {
            self.pastSearches.removeAll()
        }
        updateSearchState(to: .past)
    }
}
