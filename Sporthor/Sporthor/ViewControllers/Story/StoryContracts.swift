//
//  StoryContracts.swift
//  Sporthor
//
//  Created by derTurke on 28.04.2025.
//
//

import Foundation

protocol StoryPresenterProtocol: BasePresenterProtocol {
    var view: StoryPresenterDelegate? { get set }
    var interactor: StoryInteractorProtocol { get set }
    var router: StoryRouterProtocol { get set }
    var model: Story { get set }
    
    func viewDidLoad()
    func selectedStepDidFinish(_ step: Int)
    func didTapLeft()
    func didTapRight()
    func didSwipeRight()
    func didSwipeLeft()
    func ckButtonDidTap(_ tag: Int)
    func dismiss()
    func videoReadyToPlay()
    func showIndicator()
    func hideIndicator()
}

protocol StoryPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func prepareProgressView(steps: Int, selectedStep: Int, duration: TimeInterval)
    func settingButtonHidden(_ isHidden: Bool)
    func prepareHeaderView(image: String, username: String, userId: String)
    func clearCurrentMedia()
    func showStoryDetail(_ detail: StoryDetail)
    func animateStoryTransition(for direction: SwipeDirection, updates: @escaping () -> Void)
    func pauseAnimation()
    func resumeAnimation()
}

protocol StoryInteractorProtocol: BaseInteractorProtocol {
    var delegate: StoryInteractorDelegate? { get set }
    
    func watchedStory(_ request: [String: Any]) async
}

protocol StoryInteractorDelegate: BaseInteractorDelegate {
}

protocol StoryRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: StoryRoutes)
}

enum StoryRoutes {
    case dismiss(delegate: StoryDelegate? = nil,
                 model: Story,
                 actionType: StoryActionType?,
                 animated: Bool)
    case storySetting(delegate: StorySettingDelegate?, storyDetail: StoryDetail)
}

protocol StoryDelegate: AnyObject {
    func storyActionDidFinish(_ story: Story, actionType: StoryActionType?)
    func didWatchedStory(_ story: Story, index: Int)
    func didDismissStories()
}


enum StoryActionType {
    case swipeRight
    case swipeLeft
    case dismiss
}

enum SwipeDirection {
    case left
    case right
}
