//
//  CommentContracts.swift
//  Sporthor
//
//  Created by derTurke on 29.04.2025.
//
//

import Foundation

protocol CommentPresenterProtocol: BasePresenterProtocol {
    var view: CommentPresenterDelegate? { get set }
    var interactor: CommentInteractorProtocol { get set }
    var router: CommentRouterProtocol { get set }
    var comments: [CommentModel] { get set }
    
    func viewDidLoad()
    func textFieldDidChangeSelection(_ text: String)
    func textFieldDidEndEditing(_ text: String)
    func didTappedSendButton()
    func panModalDismiss()
}

protocol CommentPresenterDelegate: BasePresenterDelegate {
    func didSetTitleLabelText(_ title: String)
    func prepareUI()
    func changeInputSendButtonHidden(_ isHidden: Bool)
    func reloadData()
    func didSetProfilePhoto(_ image: String)
    func clearTextField()
}

protocol CommentInteractorProtocol: BaseInteractorProtocol {
    var delegate: CommentInteractorDelegate? { get set }
    
    func getComments(_ request: [String: Any]) async
    func addComment(_ request: [String: Any]) async
}

protocol CommentInteractorDelegate: BaseInteractorDelegate {
    func didGetComments(_ response: [CommentModel])
    func didAddComment()
}

protocol CommentRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CommentRoutes)
}

enum CommentRoutes {
}

protocol CommentViewDelegate: AnyObject {
    func reloadCommentCount(commentCount: Int,
                            postId: String)
}
