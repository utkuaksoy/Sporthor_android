//
//  CalendarAddMissionTypeSelectorViewController.swift
//  Sporthor
//
//  Created by derTurke on 28.06.2025.
//
//

import UIKit
import PanModal

final class CalendarAddMissionTypeSelectorViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: CalendarAddMissionTypeSelectorPresenterProtocol {
        get { return self.basePresenter as! CalendarAddMissionTypeSelectorPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var scrollLineView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.backgroundSub300.color
        view.setCornerRadius(2)
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.delegate = self
        tableView.dataSource = self
        tableView.backgroundColor = .clear
        tableView.showsVerticalScrollIndicator = false
        tableView.allowsSelection = false
        tableView.separatorStyle = .none
        tableView.removeEmptyCell()
        tableView.translatesAutoresizingMaskIntoConstraints = false
        return tableView
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
}

// MARK: - CalendarAddMissionTypeSelectorPresenterDelegate
extension CalendarAddMissionTypeSelectorViewController: CalendarAddMissionTypeSelectorPresenterDelegate {
    func prepareUI() {
        view.addSubview(scrollLineView)
        view.addSubview(tableView)
        
        NSLayoutConstraint.activate([
            scrollLineView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            scrollLineView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            scrollLineView.widthAnchor.constraint(equalToConstant: 40),
            scrollLineView.heightAnchor.constraint(equalToConstant: 4),
            
            tableView.topAnchor.constraint(equalTo: scrollLineView.bottomAnchor, constant: 32),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            tableView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor)
        ])
    }
}

extension CalendarAddMissionTypeSelectorViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = ButtonTableViewCell.dequeue(from: tableView, at: indexPath)
        cell.bind(delegate: self,
                  titleColor: DesignKitColorName.contentStrong900.color,
                  cornerRadius: 24,
                  borderWidth: 1,
                  borderColor: DesignKitColorName.borderSoft200.color,
                  imageTitleSpacing: 4,
                  tag: indexPath.section)
        switch indexPath.section {
        case 0:
            cell.setTitle("Yeni Takvim Etkinliği Oluştur")
            cell.setImage(Asset.clockPlus.image)
        case 1:
            cell.setTitle("Kayıtlı Şablonlardan Seç")
            cell.setImage(Asset.bookmarkPlus.image)
        default:
            break
        }
        return cell
    }
}

extension CalendarAddMissionTypeSelectorViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 48
    }
    
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        let view = UIView()
        view.backgroundColor = .clear
        return view
    }
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return 10
    }
}

extension CalendarAddMissionTypeSelectorViewController: ButtonTableViewCellDelegate {
    func didTappedButton(tag: Int, indexPath: IndexPath?) {
        presenter.didTappedButton(tag: tag, indexPath: indexPath)
    }
}

// MARK: - PanModalPresentable
extension CalendarAddMissionTypeSelectorViewController: PanModalPresentable {
    var panScrollable: UIScrollView? {
        return tableView
    }
    
    var allowsDragToDismiss: Bool {
        return true
    }

    var allowsTapToDismiss: Bool {
        return true
    }
    
    var cornerRadius: CGFloat {
        return 16
    }
    
    var panModalBackgroundColor: UIColor {
        return .black.withAlphaComponent(0.4)
    }
    
    var showDragIndicator: Bool {
        return false
    }
    
    var shortFormHeight: PanModalHeight {
        .contentHeight(284)
    }
    
    var longFormHeight: PanModalHeight {
        .maxHeight
    }
}
