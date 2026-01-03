//
//  StorySettingPresenter.swift
//  Sporthor
//
//  Created by derTurke on 11.05.2025.
//
//

import Foundation

final class StorySettingPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: StorySettingPresenterDelegate? {
        get { return self.baseView as? StorySettingPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: StorySettingInteractorProtocol {
        get { return self.baseInteractor as! StorySettingInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: StorySettingRouterProtocol {
        get { return self.baseRouter as! StorySettingRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: StorySettingPresenterDelegate,
         interactor: StorySettingInteractorProtocol,
         router: StorySettingRouterProtocol,
         delegate: StorySettingDelegate?,
         model: StoryDetail) {
        self.storySettingDelegate = delegate
        self.model = model
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    private weak var storySettingDelegate: StorySettingDelegate?
    private var model: StoryDetail
    var items: [StorySettingItems] = StorySettingItems.allCases
}

// MARK: - StorySettingPresenterProtocol
extension StorySettingPresenter: StorySettingPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
    }
    
    private func navigate(_ routes: StorySettingRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didSelectRow(at indexPath: IndexPath) {
        guard let selectedItem = items[safe: indexPath.row] else { return }
        switch selectedItem {
        case .remove:
            deleteStory()
        }
    }
    
    func deleteStory() {
        let request: [String: Any] = ["id": model.stroryId]
        Task { @MainActor in
            await interactor.deleteStory(request)
        }
    }
}

// MARK: - StorySettingInteractorDelegate
extension StorySettingPresenter: StorySettingInteractorDelegate {
    func didDeleteStory() {
        navigate(.dismiss(delegate: storySettingDelegate, model: model))
    }
}
