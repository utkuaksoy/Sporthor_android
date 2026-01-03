//
//  StoryPresenter.swift
//  Sporthor
//
//  Created by derTurke on 28.04.2025.
//
//

import Foundation
import AVKit
import CommonKit

final class StoryPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: StoryPresenterDelegate? {
        get { return self.baseView as? StoryPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: StoryInteractorProtocol {
        get { return self.baseInteractor as! StoryInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: StoryRouterProtocol {
        get { return self.baseRouter as! StoryRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: StoryPresenterDelegate,
         interactor: StoryInteractorProtocol,
         router: StoryRouterProtocol,
         delegate: StoryDelegate?,
         model: Story) {
        self.storyDelegate = delegate
        self.model = model
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    private weak var storyDelegate: StoryDelegate?
    var model: Story
    private var selectedStep: Int = 0
    private var videoDuration: TimeInterval = 0.0
}

// MARK: - StoryPresenterProtocol
extension StoryPresenter: StoryPresenterProtocol {
    func viewDidLoad() {
        view?.didSetBackgroundColor(.black)
        view?.prepareUI()
        view?.prepareHeaderView(image: model.profileImageUrl,
                                username: model.username,
                                userId: model.userId)
        showSelectedStory(isFirstOpen: true, isUpdate: false)
    }
    
    private func preloadVideo(urlString: String, completion: @escaping (TimeInterval) -> Void) {
        guard let url = URL(string: urlString) else {
            completion(15.0)
            return
        }
        
        let asset = AVURLAsset(url: url)
        asset.loadValuesAsynchronously(forKeys: ["playable", "duration"]) { [weak self] in
            guard let self else { return }
            DispatchQueue.main.async { [weak self] in
                guard let _ = self else { return }
                var videoDuration: TimeInterval = 15.0
                if asset.statusOfValue(forKey: "playable", error: nil) == .loaded {
                    let duration = asset.duration.seconds
                    if duration > 0 {
                        videoDuration = duration
                    }
                }
                completion(videoDuration)
            }
        }
    }
    
    private func navigate(_ routes: StoryRoutes) {
        router.handleRouter(routes)
    }
    
    func selectedStepDidFinish(_ step: Int) {
        watchedStory(selectedStep)
        self.selectedStep = step
        if model.details.count > step {
            showSelectedStory()
        } else {
            didSwipeRight()
        }
    }
    
    func didTapLeft() {
        watchedStory(selectedStep)
        selectedStep -= 1
        if selectedStep >= 0 {
            showSelectedStory()
        } else {
            didSwipeLeft()
        }
    }
    
    func didTapRight() {
        watchedStory(selectedStep)
        selectedStep += 1
        if model.details.count > selectedStep {
            showSelectedStory()
        } else {
            didSwipeRight()
        }
    }
    
    func didSwipeLeft() {
        watchedStory(selectedStep)
        storyDelegate?.storyActionDidFinish(model, actionType: .swipeLeft)
    }
    
    func didSwipeRight() {
        watchedStory(selectedStep)
        storyDelegate?.storyActionDidFinish(model, actionType: .swipeRight)
    }
    
    private func showSelectedStory(isFirstOpen: Bool = false,
                                   isUpdate: Bool = false) {
        if isFirstOpen {
            self.selectedStep = model.details.firstIndex(where: { !$0.isWatched }) ?? 0
        } else if isUpdate {
            if model.details.allSatisfy({ $0.isWatched }) {
                self.selectedStep = model.details.count - 1
            } else {
                self.selectedStep = model.details.firstIndex(where: { !$0.isWatched }) ?? 0
            }
        }
        
        guard model.details.indices.contains(self.selectedStep) else { return }

        let detail = model.details[self.selectedStep]
        let duration = getDuration(for: detail)

        if let media = detail.media, media.type == MediaType.video.rawValue {
            preloadVideo(urlString: media.url) { [weak self] videoDuration in
                guard let self else { return }
                self.videoDuration = videoDuration
                self.view?.showStoryDetail(detail)
            }
        } else {
            view?.showStoryDetail(detail)
            view?.prepareProgressView(steps: model.details.count,
                                      selectedStep: self.selectedStep,
                                      duration: duration)
        }
        checkShowOwnStory()
    }
    
    private func getDuration(for detail: StoryDetail) -> TimeInterval {
            guard let firstMedia = detail.media else {
                return 5.0
            }
            if firstMedia.type == MediaType.image.rawValue {
                return 15.0
            } else if firstMedia.type == MediaType.video.rawValue {
                return 60.0
            }
            return 15.0
        }
    
    func dismiss() {
        watchedStory(selectedStep)
        view?.clearCurrentMedia()
        navigate(.dismiss(delegate: storyDelegate,
                          model: model,
                          actionType: .dismiss,
                          animated: true))
    }
    
    func update(with story: Story?, direction: SwipeDirection) {
        guard let story else {
            dismiss()
            return
        }
        
        self.model = story
        
        selectedStep = story.details.firstIndex(where: { !$0.isWatched }) ?? 0
        
        view?.animateStoryTransition(for: direction) { [weak self] in
            guard let self else { return }
            self.view?.prepareHeaderView(image: story.profileImageUrl,
                                         username: story.username,
                                         userId: story.userId)
            self.showSelectedStory(isFirstOpen: false, isUpdate: true)
        }
    }
    
    private func watchedStory(_ index: Int) {
        guard let detail = model.details[safe: index],
              !detail.stroryId.isEmpty,
              !detail.isWatched else { return }
        storyDelegate?.didWatchedStory(model, index: index)
        let request: [String: Any] = ["storyId": detail.stroryId]
        Task { @MainActor in
            await interactor.watchedStory(request)
        }
    }
    
    private func checkShowOwnStory() {
        view?.settingButtonHidden(!model.isOwn)
    }
    
    func ckButtonDidTap(_ tag: Int) {
        switch tag {
        case 0: // Close
            dismiss()
        case 1: //
            view?.pauseAnimation()
            navigate(.storySetting(delegate: self, storyDetail: model.details[selectedStep]))
        default:
            break
        }
    }
    
    func videoReadyToPlay() {
        self.view?.prepareProgressView(steps: self.model.details.count,
                                       selectedStep: self.selectedStep,
                                       duration: videoDuration)
    }
    
    func showIndicator() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            BaseHelper.shared.showIndicator()
        }
    }
    
    func hideIndicator() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            BaseHelper.shared.hideIndicator()
        }
    }
}

// MARK: - StoryInteractorDelegate
extension StoryPresenter: StoryInteractorDelegate {}

// MARK: - StorySettingDelegate
extension StoryPresenter: StorySettingDelegate {
    func deleteStory(_ model: StoryDetail) {
        view?.resumeAnimation()
        dismiss()
    }
}
