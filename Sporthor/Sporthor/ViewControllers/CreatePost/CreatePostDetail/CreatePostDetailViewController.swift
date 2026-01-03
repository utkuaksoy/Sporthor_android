//
//  CreatePostDetailViewController.swift
//  Sporthor
//
//  Created by derTurke on 23.04.2025.
//
//

import UIKit
import ComponentKit

final class CreatePostDetailViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: CreatePostDetailPresenterProtocol {
        get { return self.basePresenter as! CreatePostDetailPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.dataSource = self
        tableView.delegate = self
        tableView.separatorStyle = .none
        tableView.allowsSelection = false
        tableView.showsVerticalScrollIndicator = false
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 12, right: 0)
        tableView.backgroundColor = .clear
        return tableView
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(
            delegate: self,
            title: "Paylaş",
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 23,
            font: .bold03Compact)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        presenter.viewWillAppear()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        navigationItem.hidesBackButton = false
    }
}

// MARK: - CreatePostDetailPresenterDelegate
extension CreatePostDetailViewController: CreatePostDetailPresenterDelegate {
    override func didSetBackgroundColor(_ color: UIColor) {
        view.backgroundColor = .black
    }
    
    func prepareNavigationDelegate() {
        if let navCon = navigationController as? CustomNavigationController {
            navCon.navigationItem.hidesBackButton = true
            navCon.customDelegate = self
        }
    }
    
    func prepareNavigationBar() {
        if let navCon = navigationController as? CustomNavigationController {
            navCon.isBackWhiteExist = true
            let titleAttributes: [NSAttributedString.Key: Any] = [
                .foregroundColor: UIColor.white,
                .font: UIFont.bold03Compact
            ]
            navCon.navigationBar.titleTextAttributes = titleAttributes
        }
    }
    
    func prepareUI() {
        view.addSubview(tableView)
        view.addSubview(continueButton)
        
        NSLayoutConstraint.activate([
            tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 12),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            tableView.bottomAnchor.constraint(equalTo: continueButton.topAnchor),
            
            continueButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -24),
            continueButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            continueButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            continueButton.heightAnchor.constraint(equalToConstant: 46)
        ])
    }
    
    func beginUpdates() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            tableView.beginUpdates()
            tableView.endUpdates()
        }
    }
}

// MARK: - UITableViewDataSource & UITableViewDelegate
extension CreatePostDetailViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case 0:
            let cell = SliderTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(presenter.model)
            return cell
        case 1:
            let cell = TextViewTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(delegate: self,
                      textColor: DesignKitColorName.contentWeak200.color,
                      placeholder: "Açıklama Ekle",
                      placeholderColor: DesignKitColorName.contentWeak200.color,
                      font: .body03Compact,
                      padding: 16)
            return cell
        default:
            return UITableViewCell()
        }
    }
}

// MARK: - UITableViewDelegate
extension CreatePostDetailViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let view = UIView()
        view.backgroundColor = .clear
        return view
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        guard section != 1 else { return 24 }
        return 0
    }
    
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        return CKHeaderView(backgroundColor: DesignKitColorName.contentSub800.color,
                            leadingCons: 16,
                            trailingCons: -16)
    }
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        guard section != 1 else { return 1 }
        return 0
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        guard indexPath.section != 1 else { return 150 }
        return UITableView.automaticDimension
    }
}

// MARK: - CKButtonDelegate
extension CreatePostDetailViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedShareButton()
    }
}

// MARK: - TextViewTableViewCellDelegate
extension CreatePostDetailViewController: TextViewTableViewCellDelegate {
    func textViewDidEndEditing(_ text: String, tag: Int) {
        presenter.textViewDidEndEditing(text, tag: tag)
    }
    
    func textViewDidChange(_ text: String, tag: Int) {
        presenter.textViewDidChange(text, tag: tag)
    }
}

extension CreatePostDetailViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        switch type {
        case .back:
            presenter.didTappedBackButton()
        default:
            break
        }
    }
}
